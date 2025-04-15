<?php

$sql="select dt_id, dt_datum, dt_jaar, dt_maand, dt_maand_num, dt_dag FROM Datums";

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