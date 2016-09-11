require 'aws-sdk'

region = 'ap-southeast-2'
s3 = Aws::S3::Resource.new(region: region)

s3.buckets.limit(50).each do |b|
  puts "#{b.name}"
end


rds = Aws::RDS::Resource.new(region: 'ap-southeast-2')
      
rds.db_instances.each do |i|
  puts "Name (ID): #{i.id}"
  puts "Status   : #{i.db_instance_status}"
  puts
end

sqs = Aws::SQS::Client.new(region: 'us-west-2')

queues = sqs.list_queues

queues.queue_urls.each do |url|
  puts 'URL:                ' + url

  # Get ARN, messages available, and messages in flight for queue
  req = sqs.get_queue_attributes(
    {
      queue_url: url, attribute_names: 
        [
          'QueueArn', 
          'ApproximateNumberOfMessages', 
          'ApproximateNumberOfMessagesNotVisible'
        ]
    }
  )

  arn = req.attributes['QueueArn']
  msgs_available = req.attributes['ApproximateNumberOfMessages']
  msgs_in_flight = req.attributes['ApproximateNumberOfMessagesNotVisible']

  puts 'ARN:                ' + arn
  puts 'Messages available: ' + msgs_available
  puts 'Messages in flight: ' + msgs_in_flight
  puts
end



