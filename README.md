This api allows you to store files in a Hadoop cluster.
 * For the test of this API, I used 2 NameNodes and three data nodes that are placed on 5 machines, the flows are sent to the active NameNode that is chosen automatically and it manages communication with datanodes blocks .
 * User should indicate the address of the cluster in core-site. xml, and hdfs-core. xml.
 * If client don't like to use high availability cluster, we should indicate the hdfs url in config.properties file
 * It is tested locally 
 * If you want to send a stream remotely, you just use a web service which receives a file and use this API.
 
 * Observer design pattern to notify the progress of upload.
 
 for more details you can email me on my contacts riadh-haddad@hotmail.com
