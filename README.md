# Time App Architecture

## Requirements

### Functional Requirements

* **World Clock**: Display current time for various global cities.
* **Alarm**: Set alarms to ring at specific times; support add, modify, and delete operations.
* **Stopwatch**: Start, pause, and reset; counts elapsed time from 0 seconds.
* **Timer**: Set duration, start counting down, pause, and reset to the original duration.

### Non-Functional Requirements

* **Low Latency**: Real-time display updates with minimal delay.

## App Architecture

### Diagram

Each functionality has its own MVVM architecture.

```
WorldClockUI <---> WorldClockViewModel <---> WorldClockService
AlarmUI <---> AlarmViewModel <---> AlarmService
StopwatchUI <---> StopwatchViewModel
TimerUI <---> TimerViewModel
```

**Note**: Stopwatch and Timer logic are managed within the ViewModel for simplicity (no persistent data).

## Modules

### UI (Jetpack Compose)

* **WorldClockScreen**

    * Display a list of cities with their current times.
    * Button to add a new city.

* **AlarmScreen**

    * Display existing alarms.
    * Buttons for adding, modifying, and deleting alarms.

* **StopwatchScreen**

    * Display elapsed time.
    * Buttons: Start, Pause, Reset.

* **TimerScreen**

    * Display countdown time.
    * Buttons: Start, Pause, Reset.
    * Input for setting initial timer duration.

### ViewModel

* **WorldClockViewModel**

    * Manages state: list of cities with current times.
    * Handles adding new cities.

* **AlarmViewModel**

    * Manages state: list of alarms.
    * Handles add, modify, and delete actions.

* **StopwatchViewModel**

    * Manages stopwatch time state.
    * Handles start, pause, and reset actions.

* **TimerViewModel**

    * Manages countdown timer state.
    * Handles set, start, pause, and reset actions.

### Service

* **WorldClockService**

    * Provides accurate current time per selected city using timezone APIs.

* **AlarmService**

    * Stores alarms persistently.
    * Manages alarm lifecycle (background scheduling, triggering at specified times).

## Data Schema

* **CityAndTime**

    * `city`: String
    * `timezone`: String
    * `time`: Flow<LocalTime>

* **Alarm**

    * `id`: Int
    * `time`: LocalTime
    * `enabled`: Boolean

## Notes

### Edge Cases & Error Handling

* Prevent adding duplicate cities to World Clock.
* Prevent setting duplicate alarms at identical times.
* Handle invalid time inputs gracefully.
