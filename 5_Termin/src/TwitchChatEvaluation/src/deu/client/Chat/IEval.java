package deu.client.Chat;

/**
 *
 * @author dima
 */
public interface IEval {
    
    
    public void calcEvaluation();
    public boolean isRunning();
    public Thread getThread();
    public long getWordCount();
}
