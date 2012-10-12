package info;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

public class Statistics {

	public static void main(String... args) {
		OWLOntology ontology = loadOntology("/users/yzhou/workspace/QueryAnswering/input/uobm/univ1.owl");
		HashMap<String, Integer> stat = new HashMap<String, Integer>();
		int crossLink = 0;

		for (OWLOntology onto: ontology.getImportsClosure())
			for (OWLAxiom axiom: onto.getAxioms())
				if (!axiom.getIndividualsInSignature().isEmpty()) 
					if (axiom instanceof OWLClassAssertionAxiom) {
						OWLClassAssertionAxiom classAxiom = (OWLClassAssertionAxiom) axiom;
						updateStatistics(stat, classAxiom.getClassExpression().toString());
					}
					else if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
						OWLObjectPropertyAssertionAxiom objectAxiom = (OWLObjectPropertyAssertionAxiom) axiom;
						updateStatistics(stat, objectAxiom.getProperty().toString());
						OWLIndividual sub = objectAxiom.getSubject(), obj = objectAxiom.getObject();
						if (isCrossLink(sub, obj)) ++crossLink;
					}
					else if (axiom instanceof OWLDataPropertyAssertionAxiom) {
						
					}
					else {
						System.out.println(axiom);
					}
			
		System.out.println("CorssLink " + crossLink);
		
		for (Entry<String, Integer> entry: stat.entrySet()) 
			System.out.println(entry.getKey() + " " + entry.getValue());
	}

	public static OWLOntology loadOntology(String fileName) {
		AutoIRIMapper mapper = new AutoIRIMapper(new File(fileName.substring(0, fileName.lastIndexOf('/'))), false);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.addIRIMapper(mapper);
		System.out.println(mapper.getDocumentIRI(IRI.create("http://semantics.crl.ibm.com/univ-bench-dl.owl")));
		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(fileName));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return ontology;
	}
	
	private static boolean isCrossLink(OWLIndividual sub, OWLIndividual obj) {
		String iri1 = sub.toString(), iri2 = obj.toString();
		if (!iri1.contains("epartment") && !iri1.contains("ollege")) return false;
		if (!iri2.contains("epartment") && !iri2.contains("ollege")) return false;
		if (iri1.contains("epartment") && iri2.contains("ollege")) return true;
		if (iri2.contains("epartment") && iri1.contains("ollege")) return true;
		
		String str = iri1.contains("epartment") ? "epartment" : "ollege";
		int num1 = getNumberAfter(str, iri1);
		int num2 = getNumberAfter(str, iri2);
		return num1 != num2;
	}

	private static int getNumberAfter(String s, String t) {
		int start = t.indexOf(s) + s.length(), end = start;
		while (Character.isDigit(t.charAt(end))) ++end;
		return Integer.parseInt(t.substring(start, end));
	}

	private static void updateStatistics(HashMap<String, Integer> stat,	String string) {
		int newValue = 0;
		if (stat.containsKey(string))
			newValue = stat.get(string);
		newValue++;
		stat.put(string, newValue);
	}
}
