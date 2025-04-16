<?php
check_required_fields(["kl_naam", "kl_voornaam", "kl_email", "kl_wachtwoord", "kl_isAdmin"]);

if(!$stmt = $conn->prepare("insert into Klanten (kl_naam, kl_voornaam, kl_email, kl_wachtwoord, kl_isAdmin) values (?,?,?,?,?)")){
	die('{"error":"Prepared Statement failed on prepare","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}

if(!$stmt -> bind_param("ssssi", $postvars['kl_naam'], $postvars['kl_voornaam'],  $postvars['kl_email'], $postvars['kl_wachtwoord'], $postvars['kl_isAdmin'])){
	die('{"error":"Prepared Statement bind failed on bind","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> execute();

if($conn->affected_rows == 0) {
	$stmt -> close();
	die('{"error":"Prepared Statement failed on execute : no rows affected","errNo":' . json_encode($conn -> errno) .',"mysqlError":' . json_encode($conn -> error) .',"status":"fail"}');
}
$stmt -> close();

$kl_id = $conn -> insert_id;
die('{"data":"ok","message":"Record added successfully","status":200, "bk_code": ' . $kl_id . '}');
?>