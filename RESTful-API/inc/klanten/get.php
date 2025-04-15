<?php

$sql="select kl_id, kl_naam, kl_voornaam, kl_email, kl_wachtwoord, kl_isAdmin FROM Klanten";

$result = $conn -> query($sql);

if (!$result) {
	$response['code'] = 7;
	$response['status'] = $api_response_code[$response['code']]['HTTP Response'];
	$response['data'] = $conn->error;
	deliver_response($response);
}

$response['data'] = getJsonObjFromResult($result); 

$result->free();

$conn->close();
deliver_JSONresponse($response);

exit;
?>