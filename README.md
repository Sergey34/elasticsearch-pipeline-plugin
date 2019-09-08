#Overview
Plugin to stream data from the es index (input), apply the action (filter, join, output), in the end pipeline can be made set final action.

## Configuration

### Example
```json
{
  "title": "my pipeline title",
  "group": "my pipeline group",
  "description": "my pipeline description",
  "triggers": [
    {
      "title": "my title",
      "group": "my group",
      "description": "my description",
      "cron": "* * * * *"
    },
    {
      "title": "my title2",
      "group": "my group",
      "description": "my description",
      "cron": "* * * * *"
    },
    {
      "title": "my title3",
      "group": "my group2",
      "description": "my description",
      "cron": "* * * * *"
    }
  ],
  "configuration": {
    "sync_pipeline": true,
    "input_action": {
      "class": "com.seko0716.es.plugin.pipeline.actions.input.DemoInput"
    },
    "filter_actions": [
      {
        "class": "com.seko0716.es.plugin.pipeline.actions.filter.RandomFilterErr",
        "order": 1
      },
      {
        "class": "com.seko0716.es.plugin.pipeline.actions.filter.RandomFilterErr",
        "order": 3
      }
    ],
    "join_actions": [
    ],
    "output_actions": [
      {
        "class": "com.seko0716.es.plugin.pipeline.actions.output.ConsoleOutput",
        "order": 2
      },
      {
        "class": "com.seko0716.es.plugin.pipeline.actions.output.ErrorConsoleOutput",
        "order": 4
      }
    ],
    "final_actions": [
      {
        "class": "com.seko0716.es.plugin.pipeline.actions.finish.ConsoleFinish",
        "order": 1
      }
    ]
  }
}
```