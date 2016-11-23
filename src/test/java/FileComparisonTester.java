import org.junit.Test;
import fileComparison.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by neilwalkinshaw on 21/11/2016.
 */
public class FileComparisonTester {

    String pathToSourceDirectory; // e.g. "/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main"
    String pathToFileA; //e.g. "/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main/java/org/apache/commons/compress/archivers/zip/X0015_CertificateIdForFile.java"
    String pathToFileB; //e.g. ""/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main/java/org/apache/commons/compress/archivers/zip/X0016_CertificateIdForCentralDirectory.java"

    @Test
    public void testComparisonMatrix() throws IOException {
        File root = new File(pathToSourceDirectory);
        DuplicateDetector dd = new DuplicateDetector(root,".java");
        double[][] comparisonMatrix = dd.fileComparison(true);
        TablePrinter.printRelations(comparisonMatrix,new File("fileComparisons.csv"),dd.getFiles());
    }

    @Test
    public void testDetailedComparisonMatrix() throws IOException {
        File from = new File(pathToFileA);
        File to = new File(pathToFileB);
        FileComparator fc = new FileComparator(from,to);
        boolean[][] comparisonMatrix = fc.detailedCompare();
        TablePrinter.printRelations(comparisonMatrix,new File("detailedComparisons.csv"));
    }
}
