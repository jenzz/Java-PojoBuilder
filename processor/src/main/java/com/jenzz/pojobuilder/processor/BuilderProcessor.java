package com.jenzz.pojobuilder.processor;

import com.google.auto.service.AutoService;
import com.jenzz.pojobuilder.api.Builder;
import com.jenzz.pojobuilder.api.Ignore;
import com.jenzz.pojobuilder.processor.expections.RuleException;
import com.jenzz.pojobuilder.processor.expections.UnnamedPackageException;
import com.jenzz.pojobuilder.processor.rules.NonAbstractClassRule;
import com.jenzz.pojobuilder.processor.rules.NonPrivateFieldsRule;
import com.jenzz.pojobuilder.processor.rules.NonPrivateNoArgsConstructorRule;
import com.jenzz.pojobuilder.processor.rules.Rule;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.jenzz.pojobuilder.processor.Utils.packageName;
import static com.squareup.javapoet.JavaFile.builder;
import static javax.lang.model.SourceVersion.latestSupported;

@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {

  public static final String ANNOTATION = "@" + Builder.class.getSimpleName();

  private static final Rule[] RULES =
      new Rule[] {
          new NonAbstractClassRule(),
          new NonPrivateNoArgsConstructorRule(),
          new NonPrivateFieldsRule()
      };

  private final ElementValidator elementValidator = new ElementValidator(RULES);

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    Messager.init(processingEnv);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return new HashSet<String>() {{
      add(Builder.class.getCanonicalName());
      add(Ignore.class.getCanonicalName());
    }};
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Builder.class)) {

      // annotation is only allowed on classes, so we can safely cast here
      TypeElement annotatedClass = (TypeElement) annotatedElement;
      try {
        elementValidator.validate(annotatedClass);
      } catch (RuleException e) {
        Messager.error(annotatedClass, e.getMessage());
        return true;
      }

      try {
        generateCode(annotatedClass);
      } catch (UnnamedPackageException | IOException e) {
        Messager.error(annotatedElement, "Couldn't generate Builder for %s: %s", annotatedClass,
            e.getMessage());
      }
    }

    return true;
  }

  private void generateCode(TypeElement annotatedClass)
      throws UnnamedPackageException, IOException {
    String packageName = packageName(processingEnv.getElementUtils(), annotatedClass);

    BuilderAnnotatedClass builderAnnotatedClass =
        new BuilderAnnotatedClass(packageName, annotatedClass);
    CodeGenerator codeGenerator =
        new CodeGenerator(builderAnnotatedClass, new ClassNameGenerator(packageName));
    TypeSpec generatedClass = codeGenerator.brewJava();

    JavaFile javaFile = builder(packageName, generatedClass).build();
    javaFile.writeTo(processingEnv.getFiler());
  }
}