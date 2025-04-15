<?php
check_required_fields(["ct_naam"]);

if(!$stmt = $conn->prepare("insert into Categorieën (ct_naam) values (?)")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("s", $postvars['ct_naam'])){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();

$ct_id = $conn -> insert_id;
die('{"data":"ok","message":"Record added successfully","status":200, "bk_code": ' . $ct_id . '}');
?>