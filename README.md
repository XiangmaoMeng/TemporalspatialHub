--------------------------------
data
--------------------------------
1、The PPI datasets are in BioGRID et.al folders.
The network.txt file has two columns and each row represents the interacting PPI.
The proteins.txt file is all the unique proteins in the network file with the Systematic Open Reading Frame (ORF) symbols.

2、The essentialProteins.txt file contains the 1285 known Saccharomyces cerevisiae essential proteins.

3、The yeast_compartment_knowledge_full.txt file is the yeast protein subcellular localization information ,
which was downloaded from the COMPARTMET database (https://compartments.jensenlab.org/, Knowledge channel).

4、GSE3431_T12.txt file was downloaded from the NCBI GEO database (https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=GSE3431).
we obtained the final time-series expression data with 12 time points by averaging the expression value at the same time interval 
over three metabolic cycles as the expression value of each gene at that time point. 

5、The xiao_noise.txt file was obtained by the Time dependent and time independent models in reference:
* Xiao Q, Wang J, Peng X, et al. Detecting protein complexes fromactive protein interaction networks constructed with dynamic gene expression profiles[J]. Proteome Science, 2013, 11(1):S20.
--------------------------------
code
--------------------------------
1、In algorithms folder, Main.java is the function to run the degree centrality(DC) to identify essential proteins from static PPI network.

2、In dynet folder, DyXiao.java is the function to generate temporal dynamic PPI networks by integrating static PPI network with 
time course gene expression profile data.
TempMaxCalculation.java is the function to identify essential proteins from temporal dynamic PPI networks by maximum degree centrality(MDC).

3、In subnet folder, SubcellularClassify.java is the function to generate spatial dynamic PPI networks by integrating static PPI network with 
subcellular localization data.
SubcellularLsed.java is the function to identify essential proteins from spatial dynamic PPI networks by LSED method proposed in reference:
* Peng X, Wang J, Wang J, et al. Rechecking the centrality-lethality rule in the scope of protein subcellular localization interaction networks[J]. PloS one, 2015, 10(6): e0130743.

4、In dySub folder, DySubCalculation.java is the function to generate spatial dynamic PPI networks by integrating static PPI network with 
subcellular localization data and time course gene expression profile data and identify essential proteins by maximum degree centrality(MDC).

5、In analysis folder, Hub2NHub.java and Hub2NHub_2.java are used to analyse the relations among temporospatial hub,static hub and essentail proteins.
DateHub.java is used to analyse the relations among party hub, date hub and essential proteins.
--------------------------------
mxmanhui@csu.edu.cn
