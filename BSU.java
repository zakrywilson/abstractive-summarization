/**
 * Representation of a single Basic Semantic Unit
 */
public class BSU {
  
  private String actor, action, receiver, score;

  /**
   * Constructor
   * <p>
   *   Creates a Basic Semantic Unit containing a single actor-action-receiver triple.
   *   Confidence score is set to empty String.
   * </p>
   *
   * @param actor - subject
   * @param action - verb or verb phrase
   * @param receiver - direct object or object of predicate
   */
  protected BSU(final String actor, final String action, final String receiver) {
    this(actor, action, receiver, "");
  }

  /**
   * Constructor
   * <p>
   *   Creates a Basic Semantic Unit containing a single actor-action-receiver triple
   *   and a confidence score.
   * </p>
   *
   * @param actor - subject
   * @param action - verb or verb phrase
   * @param receiver - direct object or object of predicate
   * @param score - confidence score assigned to each BSU
   */
  protected BSU(final String actor, final String action, final String receiver, final String score) {
    this.actor = actor;
    this.action = action;
    this.receiver = receiver;
    this.score = score;
  }

  /**
   * Getter for actor
   * @return actor
   */
  protected String getActor() {
    return this.actor;
  }

  /**
   * Getter for action
   * @return action
   */
  protected String getAction() {
    return this.action;
  }

  /**
   * Getter for receiver
   * @return receiver
   */
  protected String getReceiver() {
    return this.receiver;
  }

  /**
   * Getter for score
   * @return score
   */
  protected String getScore() {
    return this.score;
  }

  @Override
  public String toString() {
    return "[" + this.actor    + " | " +
                 this.action   + " | " +
                 this.receiver + "]\n";
  }
}
