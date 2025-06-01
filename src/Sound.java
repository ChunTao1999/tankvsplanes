package finalProject;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	private Clip clip;
	private boolean soundLoaded;
	
	public Sound (String filePath) {
		try {
	         // Open an audio input stream.
	         File file = new File(filePath);
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
	         
	         // Get a sound clip resource, open audio clip, and 
	         // load samples from the audio input stream.
	         this.clip = AudioSystem.getClip();
	         this.clip.open(audioIn);
	         this.soundLoaded = true;
	      } 
	      catch (UnsupportedAudioFileException e) {
	         this.soundLoaded = false; 
	         e.printStackTrace();
	      } 
	      catch (IOException e) {
	         this.soundLoaded = false; 
	         e.printStackTrace();
	      } 
	      catch (LineUnavailableException e) {
	         this.soundLoaded = false; 
	         e.printStackTrace();
	      }

	}
	
	public Clip getClip() {
		return this.clip;
	}
	
	public boolean getSoundLoaded() {
		return this.soundLoaded;
	}
	
	public void play() {
		if (this.getSoundLoaded() == true)
			this.getClip().start();
	}

}
