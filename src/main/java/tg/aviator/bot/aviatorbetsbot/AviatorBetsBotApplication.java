package tg.aviator.bot.aviatorbetsbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
public class AviatorBetsBotApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.of("+02:00")));
        SpringApplication.run(AviatorBetsBotApplication.class, args);
    }

}
