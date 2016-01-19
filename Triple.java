/**
 * Representation of a single triple.
 */
public class Triple {


  private String actor, action, receiver, score;


  /**
   * Constructor.
   * Creates a Triple, containing a single actor-action-receiver triple.
   * Confidence score is set to empty String.
   * @param actor - subject
   * @param action - verb or verb phrase
   * @param receiver - direct object or object of predicate
   */
  protected Triple(final String actor, final String action, final String receiver) {
    this(actor, action, receiver, "");
  }


  /**
   * Constructor.
   * Creates a Triple containing a single actor-action-receiver triple
   * and a confidence score.
   * @param actor - subject
   * @param action - verb or verb phrase
   * @param receiver - direct object or object of predicate
   * @param score - confidence score assigned to each Triple
   */
  Triple(final String actor, final String action, final String receiver, final String score) {
    this.actor = actor;
    this.action = action;
    this.receiver = receiver;
    this.score = score;
  }


  /**
   * Getter for actor.
   * @return actor
   */
  String getActor() {
    return this.actor;
  }


  /**
   * Getter for action.
   * @return action
   */
  String getAction() {
    return this.action;
  }


  /**
   * Getter for receiver.
   * @return receiver
   */
  String getReceiver() {
    return this.receiver;
  }


  /**
   * Getter for score.
   * @return score
   */
  String getScore() {
    return this.score;
  }


  @Override
  public String toString() {
    return "[" + this.actor    + " | " +
                 this.action   + " | " +
                 this.receiver + "]\n";
  }
}