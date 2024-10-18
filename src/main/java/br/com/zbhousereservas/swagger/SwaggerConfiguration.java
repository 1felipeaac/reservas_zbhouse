//package br.com.zbhousereservas.swagger;
//
//import br.com.zbhousereservas.entities.Usuario;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//public class SwaggerConfiguration {
//    @Bean
//    public Docket zbhouseAPI(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("br.com.zbhousereservas"))
//                .paths(PathSelectors.ant("/**"))
//                .build()
//                .ignoredParameterTypes(Usuario.class);
//    }
//}
