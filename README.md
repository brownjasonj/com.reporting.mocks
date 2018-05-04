# MockRiskGenerator
This is a very simplistic simulation of events that one might expect from a trading risk system.  There are
three parts to the simulator:

1) Activity simulation - this part simulates the different activities that would trigger risks to be calculated.
the activities are:  market data changes, trading activity and business event activity (e.g., end of day)

2) Business process simulation - this simulates the processes that are undertaken as a consequence of the 
above activities.  This includes calculating the valuations and risks for trading activity and husiness events.

3) Persistence and publication - this part simulates the data storaging, indexing and publishing of the 
consequences of the activity and business processes.


#Using the simulator
The simulator 
