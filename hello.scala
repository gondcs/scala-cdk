//> using dep "software.amazon.awscdk:aws-cdk-lib:2.67.0"
//> using dep "software.constructs:constructs:10.1.265"
//> using options "-explain"

import software.amazon.awscdk.App
import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps
import software.constructs.Construct
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.Duration
import software.amazon.awscdk.services.sqs.Queue

@main def main =
  val app = new App()
  val env = sys.env
  // For more information, see https://docs.aws.amazon.com/cdk/latest/guide/environments.html  
  val maybeEnvironment =
    (env.get("CDK_DEFAULT_ACCOUNT"), env.get("CDK_DEFAULT_REGION")) match
      case (Some(accountId), Some(region)) =>
        Some (Environment.builder().account(accountId).region(region).build())
      case _ => None

  val stackName = env.getOrElse("CDK_STACK_NAME", throw new RuntimeException("Missing CDK_STACK_NAME environment variable."))

  val propsNoEnv = StackProps.builder()
  val props =
    maybeEnvironment
      .map(propsNoEnv.env).getOrElse(propsNoEnv)
      .build()

  val stack = new CdkStack(app, stackName, props);
  app.synth()

class CdkStack(val scope: Construct,
               val id: String,
               val props: StackProps) extends Stack(scope, id, props):
  // The code that defines your stack goes here
  // example resource
  val queue = Queue.Builder
    .create(this, "ExampleQueue")
    .visibilityTimeout(Duration.seconds(300))
    .build()
