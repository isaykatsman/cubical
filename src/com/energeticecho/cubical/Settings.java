package com.energeticecho.cubical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.androidapps.framework.FileIO;

public class Settings {
		//TODO: add firsttimestarted var and write out to file every time
		
		//init constants
		public static boolean soundEnabled = true;
		public static boolean musicEnabled = true;
		public static String file = ".cubical"; 
		
		//load data from file
		public static void load(FileIO files) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(files.readFile(file)));
				soundEnabled = Boolean.parseBoolean(in.readLine());
				musicEnabled = Boolean.parseBoolean(in.readLine());
			} catch (IOException e) {
				//defaults FTW
			} catch (NumberFormatException e) {
				// defaults FTW
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
				}
			}
		}
		
		//write data out to file
		public static void save(FileIO files) {
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
				out.write(Boolean.toString(soundEnabled) + "\n");
				out.write(Boolean.toString(musicEnabled));
			} catch (IOException e) {
			} finally {
				try {
					if (out != null) {
						out.close();
					} 
				} catch (IOException e) {
				}	
			}
		}
}
