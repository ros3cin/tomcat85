Requirements
- Unix environment
- R should be installed

Run the ./run_analysis.sh to see the statistical analysis results.

Description

The samples are related to the ParameterMap module of the Tomcat85.
Two samples were collected, one was obtained from using the original collection (LinkedHashMap). And the other one was obtained from using the collection recommended by CECOTool (WeakHashMap).

To collect the samples, 60 iterations were set for each one.
The first 30 iterations were dumped, to avoid JIT and garbage collection influence. The last 30 iterations were considered. So 30 samples from each were used.

The R script first uses the shapiro-wilk test to test the distributions for normality, assuming alpha 0.05. As you will see, as the p-value is lesser than 0.05, the distributions are considered not normal. 

Because of this, the kolmogorov-smirnov test is then used to test if the two samples come from the same distribution, also assuming alpha 0.05. The test again rejects this null hypothesis, and the samples are then very likely to come from different distributions.

Some effect size related test must the be used, but the difference is very apparent, as you can see from their means. And the recommend collection, WeakHashMap is very likely to perform better than the original collection (LinkedHashMap).
