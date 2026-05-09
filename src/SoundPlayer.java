import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
  public static void playSound(String fileName) {
   new Thread(() -> {
   try {
   File soundFile = new File("src/Sounds/" + fileName);
   if (!soundFile.exists()) {
   System.err.println("❌ Sound file not found: " + soundFile.getAbsolutePath());
 return;
 }
 AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
 Clip clip = AudioSystem.getClip();
 clip.open(audioStream);
  clip.start();
   } catch (UnsupportedAudioFileException e) {
       System.err.println("❌ Unsupported audio file: " + e.getMessage());
    }
   catch (IOException e) {
   System.err.println("❌ IO Exception: " + e.getMessage());
 }

 catch (LineUnavailableException e) {
   System.err.println("❌ Audio line unavailable: " + e.getMessage());
            }
 }).start();
    }
}
