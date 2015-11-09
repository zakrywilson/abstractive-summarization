import java.util.*;

public class BSU {
  
  private String actor;
  private String action;
  private String receiver;


  /**
   * Constructor
   * <p>
   * Basic Semantic Unit containing actor, action, receiver.
   *
   * @param actor
   * @param action
   * @param receiver
   */
  public BSU(final String actor, final String action, final String receiver) {
    this.actor = actor;
    this.action = action;
    this.receiver = receiver;
  }

  protected String getActor() {
    return this.actor;
  }

  protected String getAction() {
    return this.action;
  }

  protected String getReceiver() {
    return this.receiver;
  }

  @Override
  public String toString() {
    return "[" + this.actor    + " | " +
                 this.action   + " | " +
                 this.receiver + "]\n";
  }
}