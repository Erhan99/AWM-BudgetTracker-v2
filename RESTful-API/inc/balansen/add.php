<?php
check_required_fields(["bl_kl_id"]);

if(!$stmt = $conn->prepare("insert into Balansen (bl_kl_id) values (?)")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("i", $postvars['bl_kl_id'])){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();

$bl_id = $conn -> insert_id;
die('{"data":"ok","message":"Record added successfully","status":200, "bk_code": ' . $bl_id . '}');
?>