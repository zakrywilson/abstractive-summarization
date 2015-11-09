/**
 * Representation of a single Basic Semantic Unit
 */
public class BSU {
  
  private String actor, action, receiver;


  /**
   * Constructor
   * <p>
   *   Creates a Basic Semantic Unit containing a single actor-action-receiver triple.
   * </p>
   * @param actor
   * @param action
   * @param receiver
   */
  protected BSU(final String actor, final String action, final String receiver) {
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
