<?php 
$afile = "Upload File ".$_FILES["uploadedfile"]["name"]; 
echo $afile;
echo "<br>";

//get org 
$organization = $_POST['org'];
echo $organization;

echo "<br>";

// Check if the form was submitted 
if ($_SERVER["REQUEST_METHOD"] == "POST") { 
    // Check if file was uploaded without errors 
    if (isset($_FILES["uploadedfile"]) && $_FILES["uploadedfile"]["error"] == 0) { 
          
        $file_name     = $_FILES["uploadedfile"]["name"]; 
        $file_type     = $_FILES["uploadedfile"]["type"]; 
        $file_size     = $_FILES["uploadedfile"]["size"]; 
        $file_tmp_name = $_FILES["uploadedfile"]["tmp_name"]; 
        $file_error    = $_FILES["uploadedfile"]["error"]; 
          
          
	move_uploaded_file($_FILES[uploadedfile]["tmp_name"],"uploads/".$file_name);
// Now run the sql script
//echo "delete = ".file_get_contents("delete_efdremotetabs.sql");
$sql = file_get_contents("uploads/".$file_name);
//$sql .= file_get_contents("delete_efdremotetabs.sql");
//$sql .= file_get_contents("efdremote_updatefromstage.sql");

//echo $sql;
echo "<br>";
$con = mysqli_connect("localhost", "efd_remote_stage", "efd", "efdremotestage");
if (mysqli_connect_errno()) { /* check connection */
    printf("Connect failed: %s\n", mysqli_connect_error());
echo "<br>";
    exit();
}

/* execute multi query */
mysqli_query($con,"set foreign_key_checks=0");
if(mysqli_multi_query($con,$sql)) {
    echo "Successful run of SQL Upload ";
echo "<br>org = ";
echo $organization;
unlink ("uploads/".file_name);
sleep(2);
echo '<script>',
	'window.close();',
	'</script>'
;
} else {
   echo "error";
echo "<br>";
echo("Error description: " . mysqli_error($con));
echo "<br>";
}


echo "<br>";

    } 
else
echo "File upload failure";
} 


?>
