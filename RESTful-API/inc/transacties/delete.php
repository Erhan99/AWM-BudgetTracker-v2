<?php
if (!isset($_GET['id']) || empty($_GET['id'])) {
    die('{"error":"Missing transaction ID","status":"fail"}');
}

$tr_id = $conn->real_escape_string($_GET['id']);

if(!$stmt = $conn->prepare("delete from Transacties where tr_id = ?")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("i", $tr_id)){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();
die('{"data":"ok","message":"Record deleted successfully","status":200}');
?>