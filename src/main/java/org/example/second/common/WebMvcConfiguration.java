package org.example.second.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final String uploadPath;

    public WebMvcConfiguration(@Value("${file.direct}") String uploadPath){
        this.uploadPath = uploadPath ;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:" + uploadPath);
        registry.addResourceHandler("/**") //를
                .addResourceLocations("classpath:/static/")//로 맵핑
                .resourceChain(true)
                .addResolver(new PathResourceResolver(){
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);

                        if(requestedResource.exists() && requestedResource.isReadable()){
                            return requestedResource;
                        }

                        return new ClassPathResource("/static/index.html");
                    }
                });
    }

//    @Controller
//    public class WebController implements ErrorController {
//        @GetMapping({"/", "/error"})
//        public String index() {
//            return "index.html";
//        }
//    }


}
