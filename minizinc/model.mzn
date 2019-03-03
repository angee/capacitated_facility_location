% MiniZinc model for the capacitated facility location problem

int: numFacilities;
int: numClients;

set of int: Facilities = 1..numFacilities;
set of int: Clients = 1..numClients;

% the demand per client
array[Clients] of float: demand;
% the cost to open facility f
array[Facilities] of float: cost;
% the cost to open facility f
array[Facilities] of float: capacity;
% the cost if facility f services client c
array[Facilities, Clients] of float: cost_to_service;

% set to 1 if facility f is opened
array[Facilities] of var 0..1: x;
% set to 1 if the demand is met by facility f for client c
array[Facilities, Clients] of var 0..1: y;

var float: objective;


% every client needs to be served
constraint 
  forall (client in Clients) (
      sum (f in Facilities) (y[f,client]) = 1
  );
  
% facilities cannot serve more than their capacity allows them to  
constraint  
  forall (f in Facilities) (
        sum (c in Clients) (demand[c] * y[f,c]) <= capacity[f] * x[f]);
   
constraint 
  objective = sum (f in Facilities) (x[f]* cost[f]) 
            + sum (c in Clients , f in Facilities) (y[f,c] * cost_to_service[f,c]);   
   
solve minimize objective;
