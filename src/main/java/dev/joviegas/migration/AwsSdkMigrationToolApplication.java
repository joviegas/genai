package dev.joviegas.migration;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

public class AwsSdkMigrationToolApplication {
}

//@SpringBootApplication(scanBasePackages = {"dev.joviegas.migration", "dev.joviegas.rag_demp"})
//public class AwsSdkMigrationToolApplication implements CommandLineRunner {
//
//    private final AwsSdkMigrationTool migrationTool;
//
//    @Autowired
//    public AwsSdkMigrationToolApplication(AwsSdkMigrationTool migrationTool) {
//        this.migrationTool = migrationTool;
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(AwsSdkMigrationToolApplication.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Example input and output files
//        String inputFile = "/Users/joviegas/gitrepo/v1migrationtest/src/main/java/v1migrationtest/S3ObjectDateAppender.java";
//        String outputFile = "/Users/joviegas/gitrepo/v1migrationtest/src/main/java/v1migrationtest/S3ObjectDateAppender_New.java";
//
//        migrationTool.transform(args[0], args[1]);
//    }
//}
//
