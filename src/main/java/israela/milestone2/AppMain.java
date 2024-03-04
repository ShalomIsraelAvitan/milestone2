package israela.milestone2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.Push;


//@Push
@SpringBootApplication
public class AppMain {
	

	public static void main(String[] args) {
		SpringApplication.run(AppMain.class, args);
		System.out.println("server run=============>>\n");
	}

}