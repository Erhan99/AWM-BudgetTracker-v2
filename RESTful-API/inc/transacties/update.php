<?php
check_required_fields(["tr_id", "tr_bedrag", "tr_begunstigde", "tr_dt_id", "tr_ct_id"]);

if (!isset($postvars['tr_mededeling'])) {
    $postvars['tr_mededeling'] = null;
}
if (!isset($postvars['tr_img'])) {
   $postvars['tr_img'] = null;
   error_log("No image data received");
}
else{
   error_log("Received image data length: " . strlen($postvars['tr_img']));
   $decodedImage = base64_decode($postvars['tr_img']);
   error_log("Decoded image length: " . strlen($decodedImage));
   $postvars['tr_img'] = $decodedImage;
}

if(!$stmt = $conn->prepare("update Transacties SET tr_bedrag = ?,  tr_mededeling = ?, tr_begunstigde = ?, tr_img = ?, tr_dt_id = ?, tr_ct_id = ? WHERE tr_id = ?")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("isssiii", $postvars['tr_bedrag'], $postvars['tr_mededeling'], $postvars['tr_begunstigde'], $postvars['tr_img'], $postvars['tr_dt_id'],  $postvars['tr_ct_id'], $postvars['tr_id'])){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();

$tr_id = $conn -> insert_id;
die('{"data":"ok","message":"Record updated successfully","status":200, "bk_code": ' . $tr_id . '}');
?>