# 

This app allows you to store files in a hadoop cluster.

 * for the test of this API, I used 2 NameNodes and three datanodes
 * that are placed on 5 machines, the flows are sent to the active NameNode that is chosen automatically  
 * and it manages communication with datanodes blocks in terms 
 * of management ..... .,user should indicate the address of the cluster in core-site.xml,and hdfs-core.xml.
 * if client don't like to use high availability cluster ,we should indicate the hdfs url in config.properties file
 * it is tested locally 
 * if you want to send a stream remotely, you just use a 
 * web service which receives a file and in use this API.
 
 Observer design pattern to notify the progress of upload.
 
 for more details you can email me on my contacts riadh-haddad@hotmail.com
