package com.brunosottomayor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassExtractor {
    Path inputPath;

    public ClassExtractor(Path inputPath) {
        this.inputPath = inputPath;
    }

    public void extractClasses(Path outputPath) throws ClassExtractorException{
        List<String> classNames = new ArrayList<>();
        List<File> javaFiles = null;
        try {
            javaFiles = Files.walk(Paths.get(this.inputPath.toString()))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ClassExtractorException("Error while reading the input directory");
        }

        for (File file : javaFiles) {
            CompilationUnit cu;
            try {
                cu = (new JavaParser()).parse(file).getResult().get();
            } catch (IOException e) {
                throw new ClassExtractorException("Error while parsing the file " + file.getPath());
            }

            new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Void arg) {
                    if (!n.isNestedType()) { // Filtering out inner classes
                        cu.getPackageDeclaration().ifPresent(pkg -> classNames.add(pkg.getNameAsString() + "." + n.getNameAsString()));
                    }
                    super.visit(n, arg);
                }
            }.visit(cu, null);
        }

        try {
            Files.write(outputPath, classNames);
        } catch (IOException e) {
            throw new ClassExtractorException("Error while writing the output file");
        }

    }
}
