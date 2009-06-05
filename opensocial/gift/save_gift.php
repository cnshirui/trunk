<?php

require_once 'db.php';

if( isset($_POST['from']) ) {
    $from = $_POST['from'];
}
if( isset($_POST['to']) ) {
    $to = $_POST['to'];
}
if( isset($_POST['gid']) ) {
    $gid = $_POST['gid'];
}
$ts = time();

$sql = "INSERT INTO gifts (`from`, `to`, `gid`, `ts`) VALUES ($from, $to, $gid, $ts)";

echo $sql;


$db->query($sql);

?>
