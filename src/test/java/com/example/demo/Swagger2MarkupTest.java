//package com.example.demo;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.BufferedWriter;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
///**
// * @author Administrator
// * @date 2020-06-04 18:05
// */
//@RunWith(SpringRunner.class)
//@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
//public class Swagger2MarkupTest {
//    private RestTemplate restTemplate = new RestTemplate();
//
//    @Test
//    public void createSpringfoxSwaggerJson() throws Exception {
//        String baseurl = System.getProperty("custom.baseurl");
//        String swaggerUrl = "http://" + baseurl + "/v2/api-docs";
//        String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");
//        System.out.println("swaggerUrl: " + swaggerUrl);
//        System.out.println("outputDir: " + outputDir);
//
//        String swaggerJson = restTemplate.getForObject(swaggerUrl, String.class);
//
//        Files.createDirectories(Paths.get(outputDir));
//        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"), StandardCharsets.UTF_8)) {
//            writer.write(swaggerJson);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
