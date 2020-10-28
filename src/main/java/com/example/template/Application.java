package com.example.template;

import com.example.template.book.Book;
import com.example.template.book.BookRepository;
import com.example.template.book.KafkaProcessorBook;
import com.example.template.rental.KafkaProcessorRental;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableFeignClients
@EnableBinding(KafkaProcessorRental.class)
public class Application {

    public static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);

        BookRepository bookRepository = applicationContext.getBean(BookRepository.class);
        // 초기 상품 셋팅
        String[] books = {"TV", "MASK", "NOTEBOOK", "TABLE", "CLOCK"};
        int i = 1;
        for(String p : books){
            Book book = new Book();

            book.setName(p);
            book.setPrice(i*10000);
            book.setStock(i*10);
            book.setImageUrl("/goods/img/"+p+".jpg");

            i++;
            bookRepository.save(book);
        }
    }

    /*public static KafkaProcessorRental getBeanForProcessor(){
        return Application.applicationContext.getBean(KafkaProcessorRental.class);
    }*/

    // spring-data-rest
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}

