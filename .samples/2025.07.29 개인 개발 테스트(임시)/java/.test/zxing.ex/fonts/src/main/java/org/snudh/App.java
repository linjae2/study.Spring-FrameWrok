package org.snudh;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.*;

public class App {

	public static void main(String[] args) {
		//SpringApplication.run(App.class, args);
		GraphicsEnvironment ge = null;

		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();
		for(int i=0; i<fonts.length;i++){
					System.out.println(fonts[i].getFontName());
		}
	}

}
