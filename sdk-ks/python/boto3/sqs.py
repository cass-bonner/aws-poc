import boto3, sys, getopt, time

queueName = ''
opts, args = getopt.getopt(sys.argv[1:],"hq:",["queueName="])
for opt, arg in opts:
  if opt == '-h':
    print 'sqs.py -q <queueName>'
    sys.exit()
  elif opt in ("-q", "--queueName"):
    queueName = arg
    print 'QueueName supplied: ', queueName 
  else:
    print 'sqs.py -q <queueName> // queueName is required'
    sys.exit()
sqs = boto3.resource('sqs')
client = boto3.client('sqs')
response = client.create_queue(QueueName=queueName)
time.sleep(7)
response = client.list_queues()
print 'All queues: ', response

#if __name__ == "__main__":
#  main(sys.argv[1:])
