<?php
check_required_fields(["ct_id", "ct_naam"]);

if(!$stmt = $conn->prepare("update Categorieën SET ct_naam = ? WHERE ct_id = ?")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("si", $postvars['ct_naam'], $postvars['ct_id'])){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();

$ct_id = $conn -> insert_id;
die('{"data":"ok","message":"Record updated successfully","status":200, "bk_code": ' . $ct_id . '}');
?>