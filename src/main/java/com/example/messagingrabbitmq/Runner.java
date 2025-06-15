package com.example.messagingrabbitmq;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Enter a message or type 'q' to exit:");

		Scanner scanner = new Scanner(System.in);

		while (true) {
			String message = scanner.nextLine();

			if (message.equals("q")) {
				System.out.println("Shutting down...");
				break;
			}

			System.out.println("Sent <" + message + ">");

			rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.fanoutExchangeName, "", message);
			receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		}
	}

}
