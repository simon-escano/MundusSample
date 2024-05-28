package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.util.Scanner;

public class DesktopLauncher {

	public static void main (String[] arg) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter id: ");
		int id = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter color { normal, pixel, red, yellow }: ");
		String color = sc.nextLine();
		switch (color) {
			case "normal":
				color = "";
				break;
			default:
				color = "_" + color;
				break;
		}

		if (!color.isEmpty()) {
			Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
			config.setForegroundFPS(60);
			config.setTitle("MundusSample");
			config.setBackBufferConfig(8, 8, 8, 8, 24, 0,4);
			new Lwjgl3Application(new Game(id, color), config);
		}
	}
}
