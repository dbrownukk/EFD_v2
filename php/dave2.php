<?php 
$sql1 = file_get_contents("uploads/r.sql");
$sql2 = file_get_contents("delete_efdremotetabs.sql");
//$sql='select CountryName from efd_remote_stage.country';
$sql3 = file_get_contents("efdremote_updatefromstage.sql");

echo PHP_EOL;
$mysqli = new mysqli('127.0.0.1', 'efd_remote_stage', 'efd', 'efd_remote_stage');
if(!$mysqli){
echo "error connecting ";
}
if (mysqli_connect_errno()) { /* check connection */
    printf("Connect failed: %s\n", mysqli_connect_error());
	echo PHP_EOL;
    exit();
}

//echo $sql;
$mysqli->multi_query($sql1);
$mysqli->multi_query($sql2);
$mysqli->multi_query($sql3);
$mysqli->commit();
mysqli_close($mysqli);


echo PHP_EOL;

/* execute multi query */
//if ($mysqli->multi_query($sql)) {
//
 //   echo "Successful run of SQL Upload ";
//echo "<br>";
//} else {
//   echo "error";
//echo "<br>";
//echo("Error description: " . mysqli_error($mysqli));
//echo "<br>";
//}
?>
