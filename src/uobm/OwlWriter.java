
package uobm;

public class OwlWriter extends RdfWriter {
  /** abbreviation of OWL namespace */
  private static final String T_OWL_NS = "owl";
  /** prefix of the OWL namespace */
  private static final String T_OWL_PREFIX = T_OWL_NS + ":";
  private String m_ontology;

  public OwlWriter(String ontology) {
    m_ontology = ontology;
  }

  /**
   * Writes the header, including namespace declarations and ontology header.
   */
  void writeHeader() {
    String s;
    s = "xmlns:" + T_RDF_NS +
        "=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"";
    out.println(s);
    s = "xmlns:" + T_RDFS_NS + "=\"http://www.w3.org/2000/01/rdf-schema#\"";
    out.println(s);
    s = "xmlns:" + T_OWL_NS + "=\"http://www.w3.org/2002/07/owl#\"";
    out.println(s);
    s = "xmlns:" + T_ONTO_NS + "=\"" + m_ontology + "#\">";
    out.println(s);
    out.println("\n");
    s = "<" + T_OWL_PREFIX + "Ontology " + T_RDF_ABOUT + "=\"\">";
    out.println(s);
    s = "<" + T_OWL_PREFIX + "imports " + T_RDF_RES + "=\"" +
        m_ontology + "\" />";
    out.println(s);
    s = "</" + T_OWL_PREFIX + "Ontology>";
    out.println(s);
  }
}