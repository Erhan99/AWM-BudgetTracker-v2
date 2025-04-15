<?php
check_required_fields(["dt_id", "dt_datum", "dt_jaar", "dt_maand", "dt_maand_num", "dt_dag"]);

if(!$stmt = $conn->prepare("update Datums SET dt_datum = ?,  dt_jaar = ?, dt_maand = ?, dt_maand_num = ?, dt_dag = ? WHERE dt_id = ?")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("siiii", $postvars['dt_datum'], $postvars['dt_jaar'], $postvars['dt_maand'], $postvars['dt_maand_num'], $postvars['dt_dag'], $postvars['dt_id'])){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();

$dt_id = $conn -> insert_id;
die('{"data":"ok","message":"Record updated successfully","status":200, "bk_code": ' . $dt_id . '}');
?>