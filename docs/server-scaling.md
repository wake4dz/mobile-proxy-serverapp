# IBM Bluemix Cloud Server Scaling Knowledge base

Server scaling enables ShopRite backend app to handle more incoming load/traffic requesting for data. In the event of ShopRite App Push Notification campaign, once the end user received the notification message, a portion of users would take action to access ShopRite app; which would drive web service request up, resulting in app slowness due to bandwidth bottleneck. To counter the issue, server scaling helps ease the stress on server for sudden rise in traffic. Scaling will allocate more instances, or resources, to currently running server to reduce slowness in app.

## ShopRite servers in Bluemix

|  APP NAME                 | HOSTNAME           |  APP TYPE           |
|---------------------------|--------------------|---------------------|
| shopritemobileproddep01   | mybluemix.net      | Primary, Main       |
| shopritemobileproddep02   | mybluemix.net      | Secondary, Main     |
| shopritecouponproddep01   | mybluemix.net      | Primary, Coupon     |
| shopritecouponproddep02   | mybluemix.net      | Secondary, Coupon   |

## Scaling APIs:
- The APIs allow create/view/update/delete the scaling policy, view the historic scaling & percentage figures for throughput/response time.
- To get started, run the below command to get auth token to be used in the APIs' header
```sh
cf oauth-token
```
- [APIs Documentation](https://github.com/cloudfoundry/app-autoscaler/blob/master/docs/Public_API.rst)

## Scaling server commands (for manual scaling)
To scale out the server manually, run the below command, the command will scale out primary main server to 6 instances
```sh
cf scale [APP NAME] -i 6
```

To scale in the server manually, similar scale command will be used; just assign less instance to the server. The command below will scale server down to 3 instances.
```sh
cf scale [APP NAME] -i 3
```

## Auto Scaling Policy
Auto scaling eliminates the need for developer's intervention to scale the server. A policy is created to determine the conditions for server to scale out/in based on average response time, Throughput..

Following is the sample scale out policy json, setting server to scale out, increases 1 instance (capped at 5), when average throughput crosses 3 requests per second.

```sh
{
        "instance_min_count": 2,
        "instance_max_count": 5,
        "scaling_rules": [
                {
                        "metric_type": "throughput",
                        "breach_duration_secs": 60,
                        "threshold": 3,
                        "operator": ">=",
                        "cool_down_secs": 60,
                        "adjustment": "+1"
                }
        ]
}
```

Following is sample to schedule scaling on the server, after the scheduled time expired, the server will scale back down to 3 instances.
```sh
{
	"instance_min_count": 2,
	"instance_max_count": 3,
	"schedules": {
		"timezone": "America/New_York",
		"specific_date": [
			{
				"start_date_time": "2019-07-02T08:30",
				"end_date_time": "2019-07-02T22:00",
				"instance_min_count": 3,
				"instance_max_count": 7,
				"initial_min_instance_count": 6
			}
		]
	}
}
```

## Auto Scaling Policy Commands
To print server app's scaling policy
```sh
cf autoscaling-policy [APP NAME]
```

To output scaling policy to json file to current directory
```sh
cf autoscaling-policy [APP NAME] > scaling-policy.json
```

To upload scaling policy to server's app name
```sh
 cf attach-autoscaling-policy [APP_NAME] [scaling_policy_file_name].json                          
```

To update scaling policy through command line, donwload [Bluemix CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-getting-started)

Link to add/update scaling policy - [Link](https://cloud.ibm.com/docs/auto-scaling-cli-plugin?topic=auto-scaling-cli-autoscalingcli)

### Potentially useful links:
- https://cloud.ibm.com/docs/cloud-foundry-public?topic=cloud-foundry-public-autoscale_migration&locale=en
