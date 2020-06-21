package com.jedlab.userservice;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.robwin.swagger2markup.Swagger2MarkupConverter;


@RunWith(SpringJUnit4ClassRunner.class)
public class Swagger2MarkupTest
{

    @Test
    public void convertRemoteSwaggerToAsciiDoc() throws IOException
    {
        // Remote Swagger source
//        String asString = Swagger2MarkupConverter.from("http://petstore.swagger.io/v2/swagger.json").build()
//                .asString();
//        Swagger2MarkupConverter.from("http://petstore.swagger.io/v2/swagger.json").build()
//              .intoFolder("src/docs/asciidoc/generated");
//        System.out.println(asString);
        Swagger2MarkupConverter.from("http://localhost:8786/v2/api-docs").build()
      .intoFolder("src/docs/asciidoc/generated");
    }
}