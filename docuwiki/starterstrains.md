# Starter strains







## Overview

Desire to construct universal control strains so that all SGA strains have the same markers and grow on the same media. See the page on [new strains](robot/up-to-date_screen_data#genotype_of_the_new_strains_for_future_robot_work_20_07_10) for further details.


## Important strains, plasmids and primers

### Strains

See the [strain database](http://minch-moor.ncl.ac.uk/fmi/iwp/cgi?-db=FuriousFive&-startsession) for further details. All strains in the [S288c](http://wiki.yeastgenome.org/index.php/Commonly_used_strains#S288C) background unless otherwise stated. 

 | Strain number                                                                                                                                                                                                                         | Relevant genotype | Comment|
 | -------------                                                                                                                                                                                                                         | ----------------- | --------
 | DLY3798|MAT alpha can1delta::STE2pr-Sp_his5 [lyp1](http://www.yeastgenome.org/cgi-bin/locus.fpl?locus=LYP1)delta his3delta1 [leu2](http://www.yeastgenome.org/cgi-bin/locus.fpl?locus=LEU2)delta0 ura3delta0 met15delta0|Boone Y7092|
 | DLY6660|MAT a his3::KanMX his3delta1 leu2delta0 ura3delta0 met15delta0|taken from single gene deletion library|                                                                                                                      
 | DLY6739|MAT a his3::KanMX lyp1::HPH his3delta1 leu2delta0 ura3delta0 met15delta0|hph cassette transformed into DLY6660|                                                                                                              
 | DLY6740|MAT a his3::KanMX lyp1::HPH his3delta1 leu2delta0 ura3delta0 met15delta0|hph cassette transformed into DLY6660|                                                                                                              










### Plasmids

See the [plasmid database](http://minch-moor.ncl.ac.uk/fmi/iwp/cgi?-db=pDL&-loadframes) for further details. Plasmids actually stored as DNA, rather than as transformed *E. Coli*.

 | Number                                                                                                                                                                                                                                                                                                                                                                                                              | Details | Comment|
 | ------                                                                                                                                                                                                                                                                                                                                                                                                              | ------- | --------
 | pDL12|amp leu2|[pRS415](http://genome-www.stanford.edu/vectordb/vector_descrip/PRS415.html) [Genetics 1989 122:19-27](http://www.genetics.org/cgi/content/abstract/122/1/19). Region homologous to LEU2 in S288c is 2228bp. Roughly 650bp upstream of start codon and 500bp downstream of stop codon.|                                                                                                             
 | pDL1279|amp, NATMX4, URA3 CAN1|CAN1 cloned as an Xho1, 4.4kb fragment, from 535, into Sal1 site of 1223. Thus we should now have NAT and URA for positive selection and CAN and URA for negative selection. Might be useful for backcrossing and selecting against. natMX4 CEN URA3 plasmid. For PCR amplification of dominant drug resistance cassette. Nourseothricin resistance gene cloned into pAG22 (pRS316)|







### Primers

See the [oligo database](http://minch-moor.ncl.ac.uk/fmi/iwp/cgi?-db=oligo&-loadframes) for further details.

 | Number                                                                                                                                                                                                                                                          | Comment | Sequence|
 | ------                                                                                                                                                                                                                                                          | ------- | ---------
 | M2176|Forward primer which amplifiies LEU from pRS415 plasmid. Product will be used to transform LEU cassette into LYP1 locus to make a new strain for SGA. 50bp.|TGATATATGCTTTGATTATTGTCTGCGGTGCACTCAACCCTATCTCGGTC|                                          
 | M2177|Reverse primer which amplifiies LEU from pRS415 plasmid. Product will be used to transform LEU cassette into LYP1 locus to make a new strain for SGA. 50bp.|CAGTACCTGAAACCGATAGGGCCCTGGTGGTTAAGCAAGGATTTTCTTAA|                                          
 | M2308|Forward primer for LEU2 in pDL12. Primer I've ordered - 50bp prior to BamHI in LYP1 followed by first 20bp of region homologous to LEU2 in pDL12. 70bp.|TTTAGCCATTATGGGTTACTTGATATATGCTTTGATTATTGTCTGCGGTGTCGAGGAGAACTTCTAGTAT|                          
 | M2309|Reverse primer for LEU2 in pDL12. Reverse complement of 50bp downstream of BamHI in LYP1 followed by reverse complement of last 20bp of region homologous to LEU2 in pDL12. 70bp.|CCCAGGCTCCTGGATTTCTCCAGTACCTGAAACCGATAGGGCCCTGGTGGTCGACTACGTCGTAAGGCCG|


## Transforming LEU2 into LYP1 at BamHI site

Basic idea: Transform DLY6739 with some DNA PCR'd up from pDL12 using primers from Eva targeting the [BamHI](http://en.wikipedia.org/wiki/BamHI) site of LYP1. Also do DLY6740 as a backup, and DLY6660 as a control.

















### LYP1

LYP1/YNL268W on chromosome XIV from coordinates 138551 to 140386 (1836bp) plus 1000 basepairs of upstream sequence and 1000 basepairs of downstream sequence.

>YNL268W  Chr 14   from 137551 to 141386 

{{hetero:lyp1.fasta.txt|lyp1.fasta}} (save as "lyp1.fasta")

    > DNAString(lyp1)
    1836-letter "DNAString" instance
    seq: ATGGGCAGGTTTAGTAACATAATAACGTCCAATAAA...AATTTATGGGAGAAATTCTGGGCTGCTGTTGCATAG
    > translate(DNAString(lyp1))
    612-letter "AAString" instance
    seq: MGRFSNIITSNKWDEKQNNIGEQSMQELPEDQIEHE...LEDIDIDSDRREIEAIIWEDDEPKNLWEKFWAAVA*



Example R/Bioconductor session:

    library(Biostrings)
    lyp1plus=read.DNAStringSet("lyp1.fasta")
    nchar(lyp1plus)
    lyp1=substr(lyp1plus,1001,1001+140386-138551)
    nchar(lyp1)
    lyp1
    toString(reverseComplement(DNAString(lyp1)))
    paste(GENETIC_CODE[substring(lyp1,seq(1,nchar(lyp1)-2,3),seq(3,nchar(lyp1),3))],collapse="")
    translate(DNAString(lyp1))
    bamHI="GGATCC"
    matchPattern(bamHI,lyp1)
    matchPattern(reverse(bamHI),lyp1)
    substr(lyp1,750,900)




### LEU2

LEU2/YCL018W on chromosome III from coordinates 91324 to 92418 (1095bp) plus 1000 basepairs of upstream sequence and 1000 basepairs of downstream sequence.

>YCL018W  Chr 3   from 90324 to 93418  

{{hetero:leu2.fasta.txt|leu2.fasta}}


    > DNAString(leu2)
    1095-letter "DNAString" instance
    seq: ATGTCTGCCCCTAAGAAGATCGTCGTTTTGCCAGGT...GCTGTCGCCGAAGAAGTTAAGAAAATCCTTGCTTAA
    > translate(DNAString(leu2))
    365-letter "AAString" instance
    seq: MSAPKKIVVLPGDHVGQEITAEAIKVLKAISDVRSN...KKVLDAGIRTGDLGGSNSTTEVGDAVAEEVKKILA*











### pDL12/pRS415

Also have {{hetero:prs415.fasta.txt|prs415.fasta}} for analysis (6021 bp). According to these sequences, there is at least one mis-sense mutation between the wild-type LEU in S288c and that in pRS415. I hope that's not a problem...

    > prs415
    A DNAStringSet instance of length 1
      width seq                                               names               
    [1]  6021 TCGCGCGTTTCGGTGATGACGGT...GCGTATCACGAGGCCCTTTCGTC gi|416320|gb|U034...

Been reading about how [PCR](http://en.wikipedia.org/wiki/Polymerase_chain_reaction) works. Forward primer seems fine - it should copy from around 850bp upstream of the ATG, which is a bit upstream of where the genome aligns to the plasmid. Reverse primer exactly matches the last 20bp of LEU2, up to and including the stop codon (TAA). Everything seems oriented correctly, so this should PCR out LEU2 and its upstream region from the plasmid.

Original primers (M2176/M2177) are each 50 bp, and the last 20bp are used to amplify LEU2. First 30bp of the primers map back to either side of BamHI in LYP1. 

Ordered some new primers (M2308, M2309) which will hopefully cut out from the plasmid exactly the region homologous to LEU2 in S288c and will have 50bp "tails" for integrating back into LYP1 at BamHI. {{hetero:leu2-lyp1-pdl12-primers.r|R/Bioconductor script}} for analysing the genes and constructing the new primers, and corresponding {{hetero:leu2-lyp1-pdl12-primers.txt|output file}}.
 

## Crossing 6740 and 3798

Doing the mating should be easy, but not completely obvious how to do the selection. I guess by adding a couple of antibiotics to a YEPD plate... G418 and Thiolysine? Nope - no useful selectable marker in 3798... Have to first transform with a plasmid containing a selectable marker... Use "One step transformation of yeast" - transforming with plasmids is easy! ;-)


*  Need to transform DLY3798 with pDL1279 (selecting on -URA plates). Done.

*  Can then cross this new strain to DLY6740, selecting with G418 and clonNAT. Done.

*  Now we have diploids containing plasmid.

*  We next have to persuade the plasmid to drop out.

*  Just grow up a load of colonies - some will just drop it - done.

*  Have diploids without the plasmid on a G418 plate.

*  Then sporulate and disect tetrads...




