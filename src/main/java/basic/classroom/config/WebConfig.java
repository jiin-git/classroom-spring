package basic.classroom.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**", "/css/**") /*"/js/**"*/
                .addResourceLocations("classpath:/static/css/", "classpath:/static/img/", "classpath:/static/js/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)
                .noTransform()
                .mustRevalidate());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    //    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "/js/**", "/*.ico", "/error", "/img/**", "/",
//                        "/login", "/find/**", "/logout", "/create/member/**", "/ending/credit", "/error/**");
//    }
}
