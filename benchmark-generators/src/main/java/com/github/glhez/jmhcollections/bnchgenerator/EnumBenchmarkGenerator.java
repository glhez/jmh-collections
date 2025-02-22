package com.github.glhez.jmhcollections.bnchgenerator;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Test various way to convert
 *
 * @author gael.lhez
 */
@SuppressWarnings("java:S106")
public class EnumBenchmarkGenerator {
  private static final String PACKAGE_NAME = "com.github.glhez.jmhcollections.enu";
  private static final String CLASS_NAME_PATTERN = "SomeEnum%s";

  private Configuration freemarkerConfiguration;
  private final int javaMajorVersion;
  private final Path projectDirectory;

  private EnumBenchmarkGenerator(Configuration cfg, int javaMajorVersion, Path projectDirectory) {
    this.freemarkerConfiguration = cfg;
    this.javaMajorVersion = javaMajorVersion;
    this.projectDirectory = projectDirectory;
  }

  private void generate(List<SourceCases> models) throws IOException, TemplateException {
    var enumDirectory = clean(projectDirectory.resolve("src/main/java").resolve(PACKAGE_NAME.replace('.', '/')));
    var template = freemarkerConfiguration.getTemplate("EnumBenchmark.ftl");

    generateCases(freemarkerConfiguration.getTemplate("Cases.ftl"), enumDirectory, "Cases", models);
    generateCodeProvider(freemarkerConfiguration.getTemplate("CodeProvider.ftl"), enumDirectory, "CodeProvider");
    for (var model : models) {
      generateEnum(template, enumDirectory, model);
    }
  }

  private void generateCases(Template template, Path enumDirectory, String enumClassContainer, List<SourceCases> models)
      throws TemplateException, IOException {
    var jti = new CasesModel(javaMajorVersion, PACKAGE_NAME, enumClassContainer, models);
    var file = enumDirectory.resolve(jti.className() + ".java");
    generate(template, file, jti);
  }

  private void generateCodeProvider(Template template, Path enumDirectory, String codeProviderInterface) throws TemplateException, IOException {
    var jti = new CodeProviderModel(javaMajorVersion, PACKAGE_NAME, codeProviderInterface);
    var file = enumDirectory.resolve(jti.className() + ".java");
    generate(template, file, jti);
  }

  private void generateEnum(Template template, Path enumDirectory, SourceCases model) throws IOException, TemplateException {
    var jti = EnumModel.of(javaMajorVersion, model);
    var file = enumDirectory.resolve(model.enumClassName() + ".java");
    generate(template, file, jti);
  }

  private void generate(Template template, Path file, Object model) throws TemplateException, IOException {
    try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
      System.out.println("generating " + file);
      template.process(model, writer);
    }
  }

  private Path clean(Path source) throws IOException {
    if (Files.isDirectory(source)) {
      try (final Stream<Path> ss = Files.find(source, Integer.MAX_VALUE, (path, attr) -> attr.isRegularFile())) {
        ss.forEach(this::deleteFile);
      }
    } else {
      Files.createDirectories(source);
    }
    return source;
  }

  private void deleteFile(Path file) {
    try {
      System.out.println("deleting file " + file);
      Files.deleteIfExists(file);
    } catch (final IOException e) {
      System.err.println("error: could not delete <" + file + ">: " + e.getMessage());
    }
  }

  public static void main(final String[] args) throws IOException, TemplateException {
    var cfg = createConfiguration();
    var casesModel = IntStream.of(3, 5, 7, 11, 13, 17, 19, 31, 37, 47, 53, 71)
                              .mapToObj(numberOfEnumConstants -> new SourceCases(numberOfEnumConstants,
                                  CLASS_NAME_PATTERN.formatted(numberOfEnumConstants)))
                              .toList();

    for (var javaMajorVersion : new int[] { 8, 17, 21 }) {
      System.out.println("generating code for Java " + javaMajorVersion);

      var ebg = new EnumBenchmarkGenerator(cfg, javaMajorVersion, Paths.get("..").resolve(toProjectDirectory(javaMajorVersion)));
      ebg.generate(casesModel);
    }
  }

  private static String toProjectDirectory(int javaMajorVersion) {
    return "java-%s".formatted(javaMajorVersion == 8 ? "1.8" : Integer.toString(javaMajorVersion));
  }

  private static Configuration createConfiguration() {
    final Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
    cfg.setClassForTemplateLoading(EnumBenchmarkGenerator.class, "");
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setFallbackOnNullLoopVariable(false);
    return cfg;
  }

  public record SourceCases(int numberOfConstants, String enumClassName) {

  }

  public record CodeProviderModel(int javaMajorVersion, String packageName, String className) {
  }

  public record CasesModel(int javaMajorVersion, String packageName, String className, List<SourceCases> values) {
  }

  public record EnumModel(int javaMajorVersion, String packageName, String className, List<EnumConstantModel> values) {
    public static EnumModel of(int javaMajorVersion, SourceCases model) {
      return new EnumModel(javaMajorVersion, PACKAGE_NAME, model.enumClassName,
          IntStream.range(0, model.numberOfConstants()).mapToObj(EnumConstantModel::of).toList());
    }
  }

  public record EnumConstantModel(String name, String code) {
    public static EnumConstantModel of(int n) {
      return new EnumConstantModel("VALUE" + n, "@-value-" + n);
    }
  }

}
