/* Model with uniquely identified instances */

/*  One customer */


/*****************************************************************************/
/*                                                                           */
/*                       Description of the Data                             */
/*                                                                           */
/*****************************************************************************/

/* Components for the custumer */

param nbcomp, integer, >=1;
set Comp, default {1 .. nbcomp};


/* Relations between customers */
/* We assume the consistence has been verified */
set Alone, within Comp;
set Flow, within Comp cross Comp;
set Concur, within Comp cross Comp; 


/* Providers */
param nbp, integer, >= 2;
set P, default {1 .. nbp};


/* Instances */
# number of type of instances per provider
param nbti{i in P}; 
# Identifiers of types of instances
set idti{i in P, j in 1..nbti[i]};
#number of instances per type of instances assumed here constant 
param nbinstances, integer; 


/* Adequation of type of instance of a given component */
# Alpha [i,j,k] real >=0 such that
#  >=1 if Component i can be mapped on the
#         k-th type of instance of Provider j
param Alpha{i in Comp, j in P, k in 1..nbti[j]};

/*****************************************************************************/
/*                                                                           */
/*                       Costs                                               */
/*                                                                           */
/*****************************************************************************/
/* Use of Instances  */
param InstancesPrices {i in P, j in 1..nbti[i]};


/* Virtual Private Cloud */
# Cost of using a Virtual Private Cloud
# We assume that a user must use the VPC of a provider if at least it uses one 
# instance
param VPC {i in P};

/* VPN tunnel service */
param VPT {i in P};

/* Configuration Links Cost */
# cost of configuring a link ...
param CIL;
param CEL;

/*****************************************************************************/
/*                                                                           */
/*                       Variables                                           */
/*                                                                           */
/*****************************************************************************/

# X[i,j,k,l] = true (1)  iff 
# Component i is mapped on 
#      the l-th instance of
#      the k-th type of instance of
#      provider j 
var X{i in Comp, j in P, k in 1..nbti[j], l in 1 .. nbinstances}, binary;

# Use of VPC
# Y[i] = true iff the customer is using a VPC hosted by provider i
var Y{i  in P}, binary; 

# Use of VPN Tunnel
# Z[i,j] = true iff we need a VPN Tunnel between Provider i and Provider j
var Z{i in P, j in P: i!=j}, binary;

# Number of  links
# gives the number of internal links
var ZIL{(i1, i2) in Flow, j in P}, binary;
var NIL;

# External
var ZEL {(i1, i2) in Flow, j1 in P, j2 in P}, binary;
var NEL ;
###############################################################################
#  Variable costs

# Use of instances
var CostInstances ;

# use of VPC
var CostVPC;

# use of VPN Tunnel
var CostVPT;

# use of Internal Links
var CostIL;

# use of External Links
var CostEL;

# Just to have a simple way to check the use of each part
var GlobalCost;

/*****************************************************************************/
/*                                                                           */
/*                       Constraints                                         */
/*                                                                           */
/*****************************************************************************/
s.t.
# The choosen instance provides enough ressources for mapping the component 
Suitable{i in Comp, j in P, k in 1..nbti[j], l in 1 .. nbinstances}: 
	X[i, j, k, l] <= Alpha[i,j,k];


# Each component have to be place only once
Placed{i in Comp}: sum{j in P, k in 1..nbti[j], l in 1 .. nbinstances} X [i,j,k,l] = 1;

# Each instance can accept at most one component
OneCompPerInstance{j in P, k in 1..nbti[j], l in 1 .. nbinstances}: 
		sum {i in Comp} X [i,j,k,l] <= 1;


# Each Component Alone must be isolated from the others and no other component 
# should be mapped in the same Provider
AloneComponent{i1 in Alone, i2 in Comp, j in P: i1 !=i2}: 
	sum{k in 1..nbti[j],l in 1 .. nbinstances}(X[i1,j,k,l] + X[i2,j,k,l]) <= 1;


# If two components are in concurrence, they souldn't be hosted by the same provider
ConcurComponent{(i1,i2) in Concur, j in P}:
	sum{k in 1..nbti[j],l in 1 .. nbinstances}(X[i1,j,k,l] + X[i2,j,k,l]) <= 1;


# Use of VPC
UseVPC{i in Comp, j in P, k in 1..nbti[j], l in 1 .. nbinstances}: Y[j] >= X[i,j,k,l]; 

# Use of VPN Tunnel
UseVPNT{(i1,i2) in Flow, j1 in P, j2 in P: j1!=j2}:
	sum{k in 1..nbti[j1], l in 1 .. nbinstances} X[i1, j1, k, l]
	+ sum{k in 1..nbti[j2], l in 1 .. nbinstances} X[i2, j2, k, l]
	<= Z[j1, j2] + 1;


# number of internal links
Zint{(i1,i2) in Flow, j in P }: ZIL[i1, i2, j] + 1 >= 
	sum{k in 1..nbti[j], l in 1 .. nbinstances}(X[i1, j,k,l] + X[i2, j, k, l]);

NIn: NIL = sum{(i1,i2) in Flow, j in P } ZIL[i1, i2, j]; 

Zext{(i1, i2) in Flow, j1 in P, j2 in P: j1!=j2}: ZEL[i1, i2, j1, j2] + 1 >= 
	sum{k in 1..nbti[j1], l in 1 .. nbinstances}X[i1, j1,k,l] + 
	sum{k in 1..nbti[j2], l in 1 .. nbinstances}X[i2, j2, k, l];

Next: NEL = sum{(i1,i2) in Flow, j1 in P, j2 in P:j1!=j2 } ZEL[i1, i2, j1, j2];


#########################################################################################
#   Cost computations
#########################################################################################
# Cost Instances

CostInstances_C: CostInstances = 
	sum{i in Comp, j in P, k in 1..nbti[j], l in 1.. nbinstances} X[i,j,k,l] * InstancesPrices [j,k]; 

# Cost VPC
CostVPC_C: CostVPC = sum{i in P} Y[i] * VPC[i];

# Cost use of internal Links
CostIL_C: CostIL = NIL * CIL;

# Cost VPN Tunnels
CostVPNT_C: CostVPT = sum{i1 in P, i2 in P: i1!=i2} Z[i1, i2]* (VPT[i1] + VPT[i2]); 

# Cost use of external links
CostEL_C: CostEL = NEL * CEL;

# Global Cost
GlobalCost_C: GlobalCost = 
				  CostInstances 
				+ CostVPC 
				+ CostIL
				+ CostVPT
				+ CostEL
; 

/*****************************************************************************/
/*                                                                           */
/*                       Goal                                                */
/*                                                                           */
/*****************************************************************************/

minimize z: GlobalCost;
solve;


/*****************************************************************************/
/*                                                                           */
/*                       Display variable and solution                       */
/*                                                                           */
/*****************************************************************************/
#display nbcomp;
#display Comp;
#display nbp;
#display P;
# display nbti;
# display Alpha;


#display Alone;
#display Flow;
printf "%13s  %13s %13s %13s  \n", "Component", "Provider", "Instance_type", "Instance_num"  ;
printf{i in Comp, j in P, k in 1..nbti [j], h in idti[j,k], l in 1 .. nbinstances : X[i, j, k, l] != 0}: 
"%13s  %13s %13s %13g  \n", i, j, h, l  ;
#display X;
#display Y;
#display Z;
printf "%13s %13g  \n", "Global Cost : ", GlobalCost ;
#display ZIL;
#display NIL;
#display NEL;

end;

