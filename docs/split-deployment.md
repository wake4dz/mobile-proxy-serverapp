# Split Deployment

## Goal

- Deploy [pr](https://github.com/wakefern/srmobile-serverapp/pull/9) to production with minimal downtime
- Split traffic by including 1 app instance with pr changes
- monitor logs for new instance to detect errors

### Process

1. cf target -s ${target}
2. cf routes
   1. If only one app is listed for service
      1. create new_app
      2. add new_app to target_app route
3. Deploy to new_app
   1. validate app running with auth
   2. monitor newrelic
   3. monitor papertrail

## Tests

- [ ] validate auth by running postman request with specifying X-CF-APP-INSTANCE
- [ ] invalid SKU
  - [ ] Ensure that we only have logs on a single line
- [ ] check newrelic
- [ ] check papertrail