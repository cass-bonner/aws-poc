<?php
require '/usr/local/lib/php_libs/vendor/autoload.php';


$sharedConfig = [
    'profile'  => 'cass', // main acct
    'region'  => 'ap-southeast-2',
    'version' => 'latest'
];

// Create an SDK class used to share configuration across clients.
$sdk = new Aws\Sdk($sharedConfig);

// Create an Amazon S3 client using the shared configuration data.
$s3Client = $sdk->createS3();

// Send a PutObject request and get the result object.
$result = $s3Client->putObject([
    'Bucket' => 'bucket8373629394746262638349405857',
    'Key'    => 'my-key2',
    'Body'   => 'this is the second body!'
]);

// Download the contents of the object.
$result = $s3Client->getObject([
    'Bucket' => 'bucket8373629394746262638349405857',
    'Key'    => 'my-key2'
]);

// Print the body of the result by indexing into the result object.
echo $result['Body'] . "\xA";

