# ms-elevator

# Java 17

Application uses spring boot, in-memory h2 database and statemachine core from spring. Also liquibase scripts are provided for database start.

Three states of elevator are declared:
1. IDLE: when elevator just waits for sth to do. Caused by STOP event.
2. REQUESTED: when elevator is called from different level. Caused by CALL_ELEVATOR event.
3. DELIVERING: when call was made inside the elevator. Caused by PICK_LEVEL event.

When elevator complete its task targetLevel field changes to null, and state of elevator becomes IDLE again.
It's not possible to change state from REQUESTED to DELIVERING and from DELIVERING to REQUESTED.

# Available endpoints:
  # url: api/v1/elevators
  usage: checking state, current and destination level of elevators <br>
  request type: GET <br>
  expected response: 200 <br>
  sample request:
  <img width="1024" alt="Screenshot 2022-10-03 at 18 30 00" src="https://user-images.githubusercontent.com/85891362/193633770-0f96fd64-bf74-4862-af1d-a7363284da9f.png">
  # url: api/v1/elevators 
  usage: calling the closest IDLE elevator from same or different level <br>
  request type: PATCH <br>
  payload: Integer requestLevel <br>
  expected response: 204 <br>
  sample request:
  <img width="1003" alt="Screenshot 2022-10-03 at 18 34 50" src="https://user-images.githubusercontent.com/85891362/193633922-6a28275b-5273-4ff8-bcf0-db865142a3fb.png">
  # url: api/v1/elevators/{elevatorId} 
  usage: picking destination level for specific elevator <br>
  request type: PATCH <br>
  payload: Integer requestLevel <br>
  expected response: 204 <br>
  sample request:
  <img width="1009" alt="Screenshot 2022-10-03 at 18 38 05" src="https://user-images.githubusercontent.com/85891362/193634025-f697fc79-ac33-47c1-b4dc-294ff75a6d84.png">
