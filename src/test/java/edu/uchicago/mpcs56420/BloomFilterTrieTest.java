package edu.uchicago.mpcs56420;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Anuved on 11/28/2016.
 */
public class BloomFilterTrieTest {

    @Test
    public void testCustomConstructor() {
        int prevPrefixLength = Container.getPrefixLength();

        BloomFilterTrie bft1 = new BloomFilterTrie(18, 4);
        assert Container.getPrefixLength() == 6;

        BloomFilterTrie bft2 = new BloomFilterTrie(20, 6);
        assert Container.getPrefixLength() == 10;

        Container.setPrefixLength(prevPrefixLength);
        assert Container.getPrefixLength() == 4;
    }

    @Test
    public void testInsert() throws IOException {
        BloomFilterTrie bft = new BloomFilterTrie(12, 4);

        File folder = new File("db/test-xsmall");
        populateBFT(folder, bft);

        assert bft.getRoot().getUncompressedContainer().size() == 0;
        assert bft.containsKmer("AGGCTATGCTCA");
        assert bft.containsKmer("AGGCTATG");
        assert bft.containsKmer("AGGCTATGCT");
        assert bft.containsKmer("AGGCTATGCTCATAG") == false;
    }

    @Test
    public void testContains() throws IOException {
        BloomFilterTrie bft = new BloomFilterTrie(12, 4);

        File folder = new File("db/test-xsmall");
        populateBFT(folder, bft);

        assert bft.getRoot().getUncompressedContainer().size() == 0;

        ArrayList<String> bftGenomes = bft.getGenomes();
//        for (int i = 0; i < bftGenomes.size(); i++)
//            System.out.println(bftGenomes.get(i));

        ArrayList<String> genomeColors = bft.genomesContainingKmer("AGGCTATGCTCA");
        assert bftGenomes.get(0).equals(genomeColors.get(0));

        genomeColors = bft.genomesContainingKmer("GCGCTATGCTGA");
        assert bftGenomes.get(4).equals(genomeColors.get(0));
    }

    @Test
    public void testSingleFileKmers() throws IOException {

//        BloomFilterTrie bft = new BloomFilterTrie(12, 4);
        BloomFilterTrie bft = new BloomFilterTrie(63, 9);
//        BloomFilterTrie bft = new BloomFilterTrie(54, 9);
//        BloomFilterTrie bft = new BloomFilterTrie(70, 9);

//        File file = new File("db/test-xsmall/input1.fasta"); // AGGCTATGCTCA
        File file = new File("db/test-small/input1.fasta"); // AGGCTATGCTCA
        bft.insertSequence(file);
    }

    @Test
    public void testOnDB() throws IOException {
        BloomFilterTrie bft = new BloomFilterTrie(63, 9);
//        File folder = new File("db/test-small");
        File folder = new File("db/test-med");
        populateBFT(folder, bft);

        assert bft.containsKmer("agcttttcattctgactgcaacgggcaatatgtctctgtgtggattaaaaaaagagtgtctga");
        assert bft.containsKmer("aactggttacctgccgtgagtaaattaaaattttattgacttaggtcactaaatactttaacc");
        assert bft.containsKmer("tagcagcttctgaactggttacctgccgtgagtaaattaaaattttattgacttaggtcacta");
        assert bft.containsKmer("ttacagagtacacaacatccatgaaacgcattagcaccaccattaccaccaccatcaccatta");
//        assert bft.containsKmer("attacagagtacacaacatccatgaaacgcattagcaccaccattaccaccaccatcaccatt");

        assert bft.containsKmer(">CP010816.1 Escherichia coli strain BL21 (TaKaRa), complete genome", "ttacagagtacacaacatccatgaaacgcattagcaccaccattaccaccaccatcaccatta");
//        assert bft.containsKmer(">CP010816.1 Escherichia coli strain BL21 (TaKaRa), complete genome", "attacagagtacacaacatccatgaaacgcattagcaccaccattaccaccaccatcaccatt");
        assert bft.containsKmer(">U00096.3 Escherichia coli str. K-12 substr. MG1655, complete genome", "ttacagagtacacaacatccatgaaacgcattagcaccaccattaccaccaccatcaccatta");
        assert bft.containsKmer(">NC_011750.1 Escherichia coli IAI39 chromosome, complete genome", "ttacagagtacacaacatccatgaaacgcattagcaccaccattaccaccaccatcaccatta");
    }

    @Test
    public void testBenchmark() throws IOException {
        int kMerLength = 63;
        Benchmark benchmark = new Benchmark(kMerLength);
        File file = new File("db/test-small/input1.fasta");
        benchmark.insertSequence(file);
    }


    private void populateBFT(File folder, BloomFilterTrie bft) throws IOException {
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                bft.insertSequence(file);
            }
        }
    }

}
